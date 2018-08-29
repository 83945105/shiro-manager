package com.global.conf;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 事务
 *
 * @author 白超
 */
@Configuration
public class TxAnoConfig {

    @Value("${web.transaction.pointcut}")
    private String pointcut;
    @Value("${web.transaction.timeout}")
    private Integer timeout;

    @Bean("txSource")
    public TransactionAttributeSource transactionAttributeSource() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRED,
                Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        if (timeout != null) {
            requiredTx.setTimeout(timeout);
        }
        Map<String, TransactionAttribute> txMap = new HashMap<>(1);
        txMap.put("*", requiredTx);
        source.setNameMap(txMap);
        return source;
    }

    @Bean
    public AspectJExpressionPointcutAdvisor pointcutAdvisor(TransactionInterceptor txInterceptor) {
        AspectJExpressionPointcutAdvisor pointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        pointcutAdvisor.setAdvice(txInterceptor);
        pointcutAdvisor.setExpression(pointcut);
        return pointcutAdvisor;
    }

    @Bean("txInterceptor")
    TransactionInterceptor getTransactionInterceptor(PlatformTransactionManager tx) {
        return new TransactionInterceptor(tx, transactionAttributeSource());
    }
}
