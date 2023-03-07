import PlantRoutes from './routes/PlantRoutes';

const headers: any = {
   'Content-Type': 'application/json',
   'Access-Control-Allow-Origin': '*'
};

let host = 'http://localhost:' + process.env.REACT_APP_SERVER_PORT + '/api';

if(process.env.NODE_ENV === "production") host = '/api';

const plantRoutes = new PlantRoutes(host, headers);

export {
   plantRoutes,
};