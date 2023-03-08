const baseRoute = '/environment/';

class EnvironmentRoutes
{
   host: String;
   headers: any;

   constructor(host: String, headers: any)
   {
      this.host = host;
      this.headers = headers;
   }
   
   // this call is to fetch environment configurations
   async fetchEnvironments()
   {
      const response = await fetch(this.host + baseRoute, {
         method: 'GET',
         headers: this.headers
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return await data.environments;
   }
}

export default EnvironmentRoutes;