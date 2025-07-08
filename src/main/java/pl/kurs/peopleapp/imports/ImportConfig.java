package pl.kurs.peopleapp.imports;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ImportConfig {

    @Value("${import.csv.core-pool-size}")
    private int corePoolSize;

    @Value("${import.csv.max-pool-size}")
    private int maxPoolSize;

    @Value("${import.csv.queue-capacity}")
    private int queueCapacity;

    @Bean(name = "importExecutor")
    public TaskExecutor importTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("import-");
        executor.initialize();
        return executor;
    }
}
