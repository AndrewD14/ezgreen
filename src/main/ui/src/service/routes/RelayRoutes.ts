const baseRoute = '/relay/';

class RelayRoutes
{
   client: any;

   constructor(client: any)
   {
      this.client = client;
   }
   
   // this call is to fetch environment configurations
   async fetchRelays()
   {
      const response = (await this.client({
         url: baseRoute,
         method: 'GET',
      })).data;

      return response.environments;
   }

   // this call is to fetch environment configurations
   async fetchRelayConfigOption()
   {
      const response = (await this.client({
         url: baseRoute + 'configoptions',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch environment configurations
   async fetchRelaysWithDetail()
   {
      const response = (await this.client({
         url: baseRoute + 'alldetails',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch environment configurations
   async fetchOneRelayWithDetail(id: number)
   {
      const response = (await this.client({
         url: baseRoute + id,
         method: 'GET',
      })).data;

      return response;
   }
}

export default RelayRoutes;