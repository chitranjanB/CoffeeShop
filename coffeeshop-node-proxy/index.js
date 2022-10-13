const express = require("express");
const morgan = require("morgan");
const { createProxyMiddleware } = require("http-proxy-middleware");

const app = express();
app.use(morgan("dev"));

const PROXY_HOST = "localhost";
const PROXY_PORT = 80;

const HELLO_MS = "http://localhost:3032/hello";
const CODIFY_MS = "http://localhost:3031/codify";
const LINKER_MS = "http://localhost:3033/linker";

[HELLO_MS, CODIFY_MS, LINKER_MS].forEach((target) => {
  const path = target.substring(target.lastIndexOf("/"));
  app.use(
    path,
    createProxyMiddleware({
      target: target,
      changeOrigin: true,
      pathRewrite: {
        [`^${path}`]: "",
      },
    })
  );
});

app.listen(PORT, HOST, () => {
  console.log(`coffeeshop-node-proxy running at ${PROXY_HOST}:${PROXY_PORT}`);
});
