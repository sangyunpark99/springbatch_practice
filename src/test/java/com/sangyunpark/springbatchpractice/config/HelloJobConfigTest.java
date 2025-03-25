package com.sangyunpark.springbatchpractice.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBatchTest
@SpringBootTest
class HelloJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    @Qualifier("helloJob")
    private Job helloJob;

    @Test
    void helloJobTest() throws Exception {
        //given
        JobParameters jobParameter = new JobParametersBuilder(jobExplorer)
                .addString("datetime", LocalDateTime.now().toString()) // 파라미터로 datetime 넘겨주기
                .toJobParameters();

        jobLauncherTestUtils.setJob(helloJob);

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameter);

        // then
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
    }
}