package com.sangyunpark.springbatchpractice.config;

import com.sangyunpark.springbatchpractice.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CustomerFileJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job customerFileJob() {
        return new JobBuilder("customerFileJob", jobRepository)
                .incrementer(new RunIdIncrementer()) // 아이디 자동 증가
                .listener(new JobLoggerListener()) // 해당 잡에 대한 callback을 받는 클래스
                .start(customerFileStep())
                .build();
    }

    @Bean
    public Step customerFileStep() {
        return new StepBuilder("customerFileStep", jobRepository)
                .<Customer, Customer>chunk(10, transactionManager)
                .reader(customerFlatFileItemReader()) // csv 파일 읽기
                .processor(customProcessor())
                .writer(customerWriter())
                .build();
    }

    @Bean
    @StepScope // Step에서만 사용
    public FlatFileItemReader<Customer> customerFlatFileItemReader() { //
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerFilterReader")
                .resource(new ClassPathResource("customers.csv"))
                .delimited()// 콤바로 나누고
                .names("id", "name", "email") // name 읽고
                .fieldSetMapper( // Customer 클래스에 자동 매핑
                        new BeanWrapperFieldSetMapper<>() {{
                            setTargetType(Customer.class);
                        }}
                ).build();
    }

    @Bean
    public ItemProcessor<Customer, Customer> customProcessor() {
        return customer -> {
            customer.setRegisteredDate(LocalDateTime.now());
            return customer;
        };
    }

    @Bean
    public ItemWriter<Customer> customerWriter() {
        return items -> {
            for(Customer customer: items) {
                log.info("Customer 저장 : {}", customer);
            }
        };
    }
}
