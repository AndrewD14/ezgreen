const baseRoute = '/sensor/';

class SensorRoutes
{
   host: String;
   headers: any;

   constructor(host: String, headers: any)
   {
      this.host = host;
      this.headers = headers;
   }
   
   // this call is to fetch senor configs
   async fetchSensors()
   {
      const response = await fetch(this.host + baseRoute, {
         method: 'GET',
         headers: this.headers
      });

      if(!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return data.sensors;
   }

   // this call is to fetch senor configs
   async fetchSensorsWithDetails()
   {
      const response = await fetch(this.host + baseRoute + 'withalldetails', {
         method: 'GET',
         headers: this.headers
      });

      if(!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return data;
   }

   // this call is to fetch a sensor with all detai infor
   async fetchOneSensorWithDetails(id: number)
   {
      const response = await fetch(this.host + baseRoute + id,{
         method: 'GET',
         headers: this.headers
      });

      if(!response.ok) throw new Error(`An error has occured: ${response.status}`);

      const data: any = await response.json();

      return data;
   }

   async fetchSensorCalibration(board: number, port: number, serialBus: number)
   {
      const response = await fetch(this.host + baseRoute + "calibration",{
         method: 'POST',
         headers: this.headers,
         body: JSON.stringify({
            board: board,
            port: port,
            serialBus: serialBus
         })
      });

      if(!response.ok) throw new Error(`An error has occured: ${response.status}`);

      const data: any = await response.json();

      return data;
   }

   //this will save the new or edit of a sensor
   async save(type: string, board: number, port: number, lowCalibration: number | null, highCalibration: number | null,
      username: string, id: string)
   {
      const response = await fetch(this.host + baseRoute + (id ? id : ''), {
         method: 'PUT',
         headers: this.headers,
         body: JSON.stringify({
            type: type,
            board: board,
            port: port,
            lowCalibration: lowCalibration,
            highCalibration: highCalibration,
            username: username
         })
      });

      return response;
   }
}

export default SensorRoutes;