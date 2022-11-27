FROM nginx:1
ARG HOMEPAGE=/
WORKDIR /app/$HOMEPAGE
COPY nginx.conf.template /etc/nginx/templates/default.conf.template
COPY build/distributions/* ./
