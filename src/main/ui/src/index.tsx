import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { store } from './app/store';
import reportWebVitals from './reportWebVitals';
import App from './App';
import Home from './features/home/Home';
import Plant from './features/plant/Plant';
import EditPlant from './features/plant/EditPlant';
import Sensors from './features/sensor/Sensors';
import Sensor from './features/sensor/Sensor';
import EditSensor from './features/sensor/EditSensor';
import Environments from './features/environment/Environments';
import Environment from './features/environment/Environment';
import EditEnvironment from './features/environment/EditEnvironment';
import Error from './features/error/Error';
import './index.css';
import { createBrowserRouter, RouterProvider } from "react-router-dom";

const container = document.getElementById('root')!;
const root = createRoot(container);

const router = createBrowserRouter([
   {
      path: "/",
      element: <App />,
      errorElement: <Error />,
      children: [
         {
           path: "/",
           element: <Home />,
         },
         {
            path: "/plant/:id",
            element: <Plant />,
         },
         {
            path: "/plant/edit/:id?",
            element: <EditPlant />,
         },
         {
            path: "/sensor",
            element: <Sensors />,
         },
         {
            path: "/sensor/:id",
            element: <Sensor />,
         },
         {
            path: "/sensor/edit/:id?",
            element: <EditSensor />,
         },
         {
            path: "/environment",
            element: <Environments />,
         },
         {
            path: "/environment/:id",
            element: <Environment />,
         },
         {
            path: "/environment/edit/:id?",
            element: <EditEnvironment />,
         },
       ],
   },
   ]);

root.render(
  <React.StrictMode>
    <Provider store={store}>
      <RouterProvider router={router} />
    </Provider>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
