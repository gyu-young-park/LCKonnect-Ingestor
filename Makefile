DATA_DIR=./data

data: crawl youtube

crawl:
	curl -s localhost:8081/crawler | jq . > $(DATA_DIR)/crawl_data.tmp && mv $(DATA_DIR)/crawl_data.tmp $(DATA_DIR)/crawl_data.json

youtube:
	curl -s localhost:8081/youtube/playlist | jq . > $(DATA_DIR)/youtube_data.tmp && mv $(DATA_DIR)/youtube_data.tmp $(DATA_DIR)/youtube_data.json

lckdata:
	curl -s localhost:8081/lckdata | jq . > $(DATA_DIR)/lck_data.tmp && mv $(DATA_DIR)/lck_data.tmp $(DATA_DIR)/lck_data.json

db:
	docker pull mariadb
	docker run -d \
      --name mariadb \
      -e MARIADB_ROOT_PASSWORD=1234 \
      -e MARIADB_DATABASE=lck \
      -e MARIADB_USER=lck \
      -e MARIADB_PASSWORD=1234 \
      -p 3306:3306 \
      mariadb:latest