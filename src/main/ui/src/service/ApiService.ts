import PlantRoutes from './routes/PlantRoutes';
import SensorRoutes from './routes/SensorRoutes';

const headers: any = {
   'Content-Type': 'application/json',
   'Access-Control-Allow-Origin': '*'
};

let host = 'http://localhost:' + process.env.REACT_APP_SERVER_PORT + '/api';

if(process.env.NODE_ENV === "production") host = '/api';

const plantRoutes = new PlantRoutes(host, headers);
const sensorRoutes = new SensorRoutes(host, headers);

export {
   plantRoutes,
   sensorRoutes,
};