# Ajouter Node.js et les outils nécessaires
FROM jenkins/jenkins:lts

USER root

# Installer Node.js et les outils pour node-gyp
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs python3 make g++ build-essential

# Retourner à l'utilisateur Jenkins
USER jenkins

# Vérification des installations
RUN node -v && npm -v

