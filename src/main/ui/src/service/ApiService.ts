import axios from 'axios';
import PlantRoutes from './routes/PlantRoutes';
import SensorRoutes from './routes/SensorRoutes';
import EnvironmentRoutes from './routes/EnvironmentRoutes';

const headers: any = {
   'Content-Type': 'application/json',
   'Access-Control-Allow-Origin': '*'
};

let host = 'http://localhost:' + process.env.REACT_APP_SERVER_PORT + '/api';

if(process.env.NODE_ENV === "production") host = '/api';

const instance = axios.create({
   baseURL: host,
   timeout: 20000,
   headers: headers
});

const plantRoutes = new PlantRoutes(instance);
const sensorRoutes = new SensorRoutes(instance);
const environmentRoutes = new EnvironmentRoutes(instance);

export {
   plantRoutes,
   sensorRoutes,
   environmentRoutes,
};