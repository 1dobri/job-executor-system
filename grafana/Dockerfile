FROM grafana/grafana:latest

RUN grafana-cli --pluginsDir "/var/lib/grafana/plugins" plugins install marcusolsson-json-datasource

USER root
RUN chown -R 472:0 /var/lib/grafana