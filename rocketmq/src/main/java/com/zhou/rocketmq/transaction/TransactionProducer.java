package com.zhou.rocketmq.transaction;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/16 17:02
 * @Description: 创建事务性生产者  使用TransactionMQProducer类创建生产者客户端，并指定唯一的producerGroup，可以设置自定义线程池来处理检查请求。本地事务执行后，需要根据执行结果回复MQ
 *
 * 事务性消息:
 * 被认为是一个两阶段的提交消息实现，以确保分布式系统的最终一致性。
 * 事务性消息确保本地事务的执行和消息的发送可以原子地执行。
 *
 * 事务性消息有三种状态：
 * (1) TransactionStatus.CommitTransaction：提交事务，表示允许消费者消费该消息。
 * (2) TransactionStatus.RollbackTransaction：回滚事务，表示该消息将被删除，不允许消费。
 * (3) TransactionStatus.Unknown：中间状态，表示需要MQ回查才能确定状态。
 *
 *使用限制
 * (1)事务性消息没有调度和批处理支持。
 * (2) 为避免单条消息被检查次数过多，导致消息堆积到一半，我们将单条消息的检查次数默认限制为15次，但用户可以通过更改“transactionCheckMax”来更改此限制”参数在broker的配置中，如果一条消息的检查次数超过“transactionCheckMax”次，broker默认会丢弃这条消息，同时打印错误日志。用户可以通过重写“AbstractTransactionCheckListener”类来改变这种行为。
 * (3) 事务消息将在一定时间后检查，该时间由代理配置中的参数“transactionTimeout”确定。并且用户也可以在发送事务消息时通过设置用户属性“CHECK_IMMUNITY_TIME_IN_SECONDS”来改变这个限制，这个参数优先于“transactionMsgTimeout”参数。
 * (4) 一个事务性消息可能会被检查或消费不止一次。
 * (5) 提交给用户目标主题的消息reput可能会失败。目前，它取决于日志记录。高可用是由 RocketMQ 本身的高可用机制来保证的。如果要保证事务消息不丢失，保证事务完整性，推荐使用同步双写。机制。
 * (6) 事务性消息的生产者 ID 不能与其他类型消息的生产者 ID 共享。与其他类型的消息不同，事务性消息允许向后查询。MQ 服务器通过其生产者 ID 查询客户端。
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        TransactionListener transactionListener = new TransactionListenerImpl();
        TransactionMQProducer producer = new TransactionMQProducer("please_rename_unique_group_name");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            try {
                Message msg =
                        new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                                ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                System.out.printf("%s%n", sendResult);

                Thread.sleep(10);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }
        producer.shutdown();
    }
}