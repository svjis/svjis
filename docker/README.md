# SVJIS App

## Jak sestavit image

Image sestavíte následujícím příkazem:

```sh
docker build -t berk76/svjis-app:latest .
```

Sestavené image pro jednotlivé verze jsou k dispozici na [DockerHubu](https://hub.docker.com/r/berk76/svjis-app).

## Spuštění image

Porměnné prostředí

* `DB_SERVER` - databázový server
* `DB_USERNAME` - uživatelské jméno
* `DB_PASSWORD` - heslo 

```sh
docker run -e DB_SERVER=<db server> -e DB_USERNAME=<db user> -e DB_PASSWORD=<db password> -d --name svjis -p 8080:8080 berk76/svjis-app:latest
```

Pro spuštění aplikace včetně databáze postupujte dle návodu: [docker-compose](https://github.com/svjis/svjis-docker/tree/master/docker-compose).
