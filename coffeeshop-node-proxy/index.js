const express = require("express");
const morgan = require("morgan");
const { createProxyMiddleware } = require("http-proxy-middleware");

const app = express();
app.use(morgan("dev"));

const PORT = 80;
const HOST = "localhost";

const HELLO_MS = "http://localhost:3032/hello";
const CODIFY_MS = "http://localhost:3031/codify";
const LINKER_MS = "http://localhost:3033/linker";

app.use(
  "/hello",
  createProxyMiddleware({
    target: HELLO_MS,
    changeOrigin: true,
    pathRewrite: {
      [`^/hello`]: "",
    },
  })
);

app.use(
  "/codify",
  createProxyMiddleware({
    target: CODIFY_MS,
    changeOrigin: true,
    pathRewrite: {
      [`^/codify`]: "",
    },
  })
);

app.use(
  "/linker",
  createProxyMiddleware({
    target: LINKER_MS,
    changeOrigin: true,
    pathRewrite: {
      [`^/linker`]: "",
    },
  })
);

app.listen(PORT, HOST, () => {
  console.log(`Coffeeshop Node Proxy Running at ${HOST}:${PORT}`);
});
