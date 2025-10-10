# LCKonnectIngestor

**LCKonnectIngestor** is a Java application that collects and provides structured data from the League of Legends Champions Korea (LCK).  
It crawls match results, champion picks and bans, and maps them with official LCK YouTube videos to deliver a unified data source for analysis, insights, and fan engagement.

## TODO
1. crawl + youtube data -> RDB
2. mapping the crawl data and youtube video (one-to-one, many-to-many, many-to-one)
3. add scheduler code or spring batch
4. REST API to call crawl and mapping data
5. Calibrate youtube video and crawl data matching
6. Pick crawl match data