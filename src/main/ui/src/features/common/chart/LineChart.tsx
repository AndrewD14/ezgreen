import React, { useState, useEffect } from 'react';
import Chart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import moment from 'moment-timezone';
import './LineChart.css';

const options: ApexOptions = {
   chart: {
      width: "100%",
      type: "line",
      zoom: {
         enabled: true
      },
      animations: {
         enabled: true,
         easing: 'linear',
         speed: 1000,
         animateGradually: {
             enabled: true,
             delay: 150
         },
         dynamicAnimation: {
             enabled: true,
             speed: 350
         }
     },
   },
   dataLabels: {
      enabled: true
   },
   markers: {
      size: 7,
   }
};

export default function LineChart(props: any)
{
   const [value, setValue] = useState([]);

   useEffect(() => {
      let input: any = [{name: "Soil moisture", data: []}];

      input[0].data = props.readings?.map((history: any) => {
         let timestamp = moment.utc(history.read).tz(Intl.DateTimeFormat().resolvedOptions().timeZone);
         
         return [timestamp.format('MM/DD/yyyy HH:mm:ss Z'), parseFloat(history.percentage).toFixed(2)];
      });

      console.log(input)
      setValue(input);
   },
   [props]
   );

   return (
      <div id="chart">
         <Chart
            options={{...options, annotations: props.annotations, xaxis: props.xaxis, yaxis: props.yaxis} }
            type='line'
            series={value}
         />
      </div>
   );
}