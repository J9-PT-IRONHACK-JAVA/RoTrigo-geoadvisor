.PHONY: all test

export DOCKER_PATH 			:= docker
export COMPOSE_DB_FILE		:= $(DOCKER_PATH)/docker-compose.db.yml

dev: 
	@ docker-compose -f ${COMPOSE_DB_FILE} up -d

nodev:
	@ docker-compose -f ${COMPOSE_DB_FILE} down --remove-orphans --volumes

destroy:
	@ docker-compose -f ${COMPOSE_DB_FILE} down --remove-orphans --rmi all --volumes
