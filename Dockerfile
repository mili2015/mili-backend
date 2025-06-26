# -----------------------------
# Fase 1: build do JAR com Maven
# -----------------------------
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /build
COPY . /build

# Gera o JAR na pasta /build/target
RUN mvn clean package -DskipTests

# -----------------------------
# Fase 2: imagem final, leve
# -----------------------------
FROM openjdk:17-jdk-slim

# Diretório da aplicação
WORKDIR /app

# Copia apenas o JAR da fase de build
COPY --from=build /build/target/mili-backend-1.0.0.jar /app/mili-backend-1.0.0.jar

# Configurar Locale
RUN apt-get update && apt-get install -y locales && \
    sed -i -e 's/# pt_BR.UTF-8 UTF-8/pt_BR.UTF-8 UTF-8/' /etc/locale.gen && \
    echo 'LANG="pt_BR.UTF-8"' > /etc/default/locale && \
    dpkg-reconfigure --frontend=noninteractive locales && \
    update-locale LANG=pt_BR.UTF-8

ENV LANG=pt_BR.UTF-8
ENV LANGUAGE=pt_BR.UTF-8
ENV LC_ALL=pt_BR.UTF-8

# Configurar fuso horário
ENV TZ=America/Argentina/San_Luis
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Expõe a porta 10122
EXPOSE 10122

# Comando para rodar a aplicação
CMD ["java", "-jar", "mili-backend-1.0.0.jar"]
