const baseRoute = '/relay/';

class RelayRoutes
{
   client: any;

   constructor(client: any)
   {
      this.client = client;
   }
   
   // this call is to fetch all relays
   async fetchRelays()
   {
      const response = (await this.client({
         url: baseRoute,
         method: 'GET',
      })).data;

      return response.environments;
   }

   //this is to save the create or edit relay
   async save(type: number, number: number, boardId: number, relay: number, username: string, id: number | null)
   {
      console.log(boardId)
      const response = (await this.client({
         url: baseRoute + (id ? id : ''),
         method: 'PUT',
         data: JSON.stringify({
            type: type,
            number: number,
            boardId: boardId,
            relay: relay,
            username: username
         })
      })).data;

      return response;
   }

   // this call is to fetch relay configurations
   async fetchRelayConfigOption()
   {
      const response = (await this.client({
         url: baseRoute + 'configoptions',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch relays with details
   async fetchRelaysWithDetail()
   {
      const response = (await this.client({
         url: baseRoute + 'alldetails',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch a relay with details
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