# WebCrawler
Parallel Web Crawler using Java SE9. Program uses concurrent programming techniques to enhance a legacy web crawler to take advantage of multi-core architectures. The crawler reads configuration from a JSON file, downloads and parses multiple HTML documents in parallel and records popular web terms in an output file.

>Run using terminal command: java -classpath target/udacity-webcrawler-1.0.jar com.udacity.webcrawler.main.WebCrawlerMain src/main/config/sample_config.json

>The src/main/config/sample_config.json file can be changed for URL crawled, popular word count and other options.
