app-clean:
	gradle clean

app-build:
	gradle test
	gradle bootJar

compose-clean:
	docker compose rm -fs
	-docker rmi bingo:latest

compose-up: app-clean app-build compose-clean
	docker compose up

compose-down:
	docker compose down