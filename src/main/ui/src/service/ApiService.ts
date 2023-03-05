import ConfigRoutes from './routes/ConfigRoutes';

const headers: any = {
   'Content-Type': 'application/json',
   'Access-Control-Allow-Origin': '*'
};

let host = 'http://localhost:' + process.env.REACT_APP_SERVER_PORT + '/api';

if(process.env.NODE_ENV === "production") host = '/api';

const configRoutes = new ConfigRoutes(host, headers);

export {
   configRoutes,
};