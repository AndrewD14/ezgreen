import React from 'react';
import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';
import { store } from './app/store';
import reportWebVitals from './reportWebVitals';
import App from './App';
import Home from './features/home/Home';
import Plant from './features/plant/Plant';
import EditPlant from './features/plant/EditPlant';
import Sensor from './features/sensor/Sensor';
import Environment from './features/environment/Environment';
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
            path: "/sensors",
            element: <Sensor />,
          },
          {
            path: "/environments",
            element: <Environment />,
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
