package com.hulkhiretech.payments.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.entity.TransactionEntity;
import com.hulkhiretech.payments.util.converter.PaymentMethodEnumConverter;
import com.hulkhiretech.payments.util.converter.PaymentTypeEnumConverter;
import com.hulkhiretech.payments.util.converter.ProviderEnumConverter;
import com.hulkhiretech.payments.util.converter.TxnStatusEnumConverter;

@Configuration
public class AppConfig {

	@Bean
	ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(10);
		executor.setQueueCapacity(1000);
		executor.setThreadNamePrefix("Async-Task-");
		executor.initialize();

		return executor;
	}
	
	@Bean
	ModelMapper modelMapper() {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(
				MatchingStrategies.STRICT);
		
		
		Converter<Integer, String> paymentMethodEnumConverter = new PaymentMethodEnumConverter();
        Converter<Integer, String> providerEnumConverter = new ProviderEnumConverter();
        Converter<Integer, String> paymentTypeEnumConverter = new PaymentTypeEnumConverter();
        Converter<Integer, String> txnStatusEnumConverter = new TxnStatusEnumConverter();

        modelMapper.addMappings(new PropertyMap<TransactionEntity, TransactionDTO>() {
            @Override
            protected void configure() {
                using(paymentMethodEnumConverter).map(source.getPaymentMethodId(), destination.getPaymentMethod());
                using(providerEnumConverter).map(source.getProviderId(), destination.getProvider());
                using(paymentTypeEnumConverter).map(source.getPaymentTypeId(), destination.getPaymentType());
                using(txnStatusEnumConverter).map(source.getTxnStatusId(), destination.getTxnStatus());
            }
        });

		
		return modelMapper;
	}
	
	
}
