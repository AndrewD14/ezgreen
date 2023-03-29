const baseRoute = '/environment/';

class EnvironmentRoutes
{
   client: any;

   constructor(client: any)
   {
      this.client = client;
   }
   
   // this call is to fetch environment configurations
   async fetchEnvironments()
   {
      const response = (await this.client({
         url: baseRoute,
         method: 'GET',
      })).data;

      return response.environments;
   }
}

export default EnvironmentRoutes;