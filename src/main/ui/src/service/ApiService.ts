import axios from 'axios';
import EnvironmentRoutes from './routes/EnvironmentRoutes';
import PlantRoutes from './routes/PlantRoutes';
import RelayRoutes from './routes/RelayRoutes';
import SensorRoutes from './routes/SensorRoutes';


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

const environmentRoutes = new EnvironmentRoutes(instance);
const plantRoutes = new PlantRoutes(instance);
const relayRoutes = new RelayRoutes(instance);
const sensorRoutes = new SensorRoutes(instance);


export {
   environmentRoutes,
   plantRoutes,
   relayRoutes,
   sensorRoutes,   
};