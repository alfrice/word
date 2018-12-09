package com.github.alfrice.word;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(
		value = {"com.github.alfrice.word.config",
				"com.github.alfrice.word.controller",
				"com.github.alfrice.word.config",
				"com.github.alfrice.word.cache",
				"com.github.alfrice.word.service"

		}
)
public class WordApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder()
				.bannerMode(Banner.Mode.LOG)
				.sources(WordApplication.class)
				.run(args);

		System.out.println("Head over to http://localhost:8080 for the api");
	}
}
