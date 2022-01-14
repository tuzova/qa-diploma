FROM node:latest
RUN mkdir -p /gate-simulator
WORKDIR /gate-simulator
COPY package.json /gate-simulator
RUN npm install
COPY . /gate-simulator
EXPOSE 9999
CMD [ "npm", "start" ] 