const baseRoute = '/environment/';

class EnvironmentRoutes
{
   client: any;

   constructor(client: any)
   {
      this.client = client;
   }
   
   // this call is to fetch environments
   async fetchEnvironments()
   {
      const response = (await this.client({
         url: baseRoute,
         method: 'GET',
      })).data;

      return response.environments;
   }

   // this call is to fetch environment configurations
   async fetchEnvironmentsConfigOption()
   {
      const response = (await this.client({
         url: baseRoute + 'configoptions',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch environments with details
   async fetchEnvironmentsWithDetail()
   {
      const response = (await this.client({
         url: baseRoute + 'alldetails',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch an environment with details
   async fetchOneEnvironmentWithDetail(id: number)
   {
      const response = (await this.client({
         url: baseRoute + id,
         method: 'GET',
      })).data;

      return response;
   }

   //this will save the new or edit of an environment
   async save(name: string, type: number, low: number | null, high: number | null, target: number, humidity: number | null, timeStart: string | null,
      timeEnd: string | null, sensors: any[], relays: any[], username: string, id: string)
   {
      let environmentId = (await this.client({
         url: baseRoute + (id ? id : ''),
         method: 'PUT',
         data: JSON.stringify({
            name: name,
            sensorType: type,
            lowDesire: low,
            highDesire: high,
            target: target,
            humidity: humidity,
            timeStart: timeStart,
            timeEnd: timeEnd,
            username: username
         })
      })).data;

      //loops through sensors
      sensors.forEach(async (sensor: any) => {
         await this.client({
            url: '/sensor/environment/' + sensor.id,
            method: 'PUT',
            data: JSON.stringify({
               environmentId: environmentId,
               username: username
            })
         });
      });

      //loops through relays
      relays.forEach(async (relay: any) => {
         await this.client({
            url: '/relay/environment/' + relay.id,
            method: 'PUT',
            data: JSON.stringify({
               environmentId: environmentId,
               username: username
            })
         });
      });

      return environmentId;
   }
}

export default EnvironmentRoutes;