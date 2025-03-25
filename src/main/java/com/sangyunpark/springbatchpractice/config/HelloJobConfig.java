package com.sangyunpark.springbatchpractice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class HelloJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job helloJob() {
         return new JobBuilder("helloJob", jobRepository)
                 .incrementer(new RunIdIncrementer()) // 아이디 자동 증가
                 .start(helloStep())
                 .next(helloStep2())
                 .build();
    }

    @Bean
    public Step helloStep() { // Spring Batch에서 하나의 Step을 정의하는 코드
        return new StepBuilder("helloStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Hello Spring Batch");

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step helloStep2() { // Spring Batch에서 하나의 Step을 정의하는 코드
        return new StepBuilder("helloStep2", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("Hello Step2!");

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}
