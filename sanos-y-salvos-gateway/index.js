const express = require('express');
const cors = require('cors');
const { createProxyMiddleware } = require('http-proxy-middleware');

const app = express();
const PORT = 8081; // El puerto de tu Gateway

app.use(cors());
app.use(express.json());

// Proxy para el Microservicio de Identidad (Login/Registro)
app.use('/api/v1/auth', createProxyMiddleware({
    target: 'http://localhost:4001', // Asegúrate que tu micro de identidad use este puerto
    changeOrigin: true,
    pathRewrite: {
        '^/api/v1/auth': '', 
    },
}));

app.listen(PORT, () => {
    console.log(`Gateway de Sanos y Salvos en puerto ${PORT}`);
});