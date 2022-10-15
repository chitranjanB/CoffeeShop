const express = require("express");
const morgan = require("morgan");
const { createProxyMiddleware } = require("http-proxy-middleware");

const app = express();
app.use(morgan("dev"));

const PROXY_HOST = "localhost";
const PROXY_PORT = 80;

const COFFEESHOP_DASHBOARD_UI = "http://localhost:3000/coffeeshop-dashboard-ui";
const COFEESHOP_API = "http://localhost:8080/coffeeshop-api";
const H2_CONSOLE = "http://localhost:8080/coffeeshop-api/h2-console";

const extractPath = (endpoint) => endpoint.substring(endpoint.lastIndexOf("/"));

// log original request and proxied request info
const logDebugProxy = (proxyRes, req, res) => {
  const exchange = `[${req.method}] [${proxyRes.statusCode}] ${req.path} -> ${proxyRes.req.protocol}//${proxyRes.req.host}${proxyRes.req.path}`;
  console.log("exchange => ", exchange); //exchange [GET] [200] / -> http://www.example.com
};

[COFFEESHOP_DASHBOARD_UI, COFEESHOP_API, H2_CONSOLE].forEach((target) => {
  const uri = extractPath(target);
  app.use(
    uri,
    createProxyMiddleware({
      target: target,
      changeOrigin: true,
      pathRewrite: {
        [`^${uri}`]: "",
      },
      onProxyRes: logDebugProxy,
    })
  );
});

app.listen(PROXY_PORT, PROXY_HOST, () => {
  console.log(`coffeeshop-node-proxy running at ${PROXY_HOST}:${PROXY_PORT}`);
});
