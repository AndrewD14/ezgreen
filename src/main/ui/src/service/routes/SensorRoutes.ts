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
   
   // this call is for to fetch senor configs
   async fetchSensors()
   {
      const response = await fetch(this.host + baseRoute, {
         method: 'GET',
         headers: this.headers
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      console.log(data)

      return await data.sensors;
   }
}

export default SensorRoutes;