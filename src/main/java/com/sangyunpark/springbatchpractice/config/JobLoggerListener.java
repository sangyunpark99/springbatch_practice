package com.sangyunpark.springbatchpractice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) { // Job 시작하기 전
        log.info("Job 시작 : {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) { // Job을 시작한 후
        log.info("Job 종료 : {} (상태 : {})", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
        JobExecutionListener.super.afterJob(jobExecution);
    }
}
