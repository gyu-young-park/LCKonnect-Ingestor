# LCKonnectIngestor

**LCKonnectIngestor** is a Java application that collects and provides structured data from the League of Legends Champions Korea (LCK).  
It crawls match results, champion picks and bans, and maps them with official LCK YouTube videos to deliver a unified data source for analysis, insights, and fan engagement.

## TODO
1. 필요한 youtube 데이터만 가져오기 -> 현재는 모든 데이터를 가져오고 filter

## Bug list

### 2020 월즈 경기 순서 역전
`LGC vs ITZ | Play-In Stage Day1 H/L 09.25 | 2020 월드 챔피언십`과 `TL vs MAD | Play-In Stage Day1 H/L 09.25 | 2020 월드 챔피언십`의 순서가 바뀌었음.

일부 영상들의 순서가 유튜브 동영상과 매칭이 안됨
