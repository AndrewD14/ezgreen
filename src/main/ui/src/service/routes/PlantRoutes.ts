const baseRoute = '/plant/';

class PlantRoutes
{
   client: any;

   constructor(client: any)
   {
      this.client = client;
   }
   
   // this call is to fetch plant table
   async fetchPlants()
   {
      const response = (await this.client({
         url: baseRoute,
         method: 'GET',
      })).data;

      return response.plants;
   }

   // this call is to fetch create plant options
   async fetchPlantOptions()
   {
      const response = (await this.client({
         url: baseRoute + "configoptions",
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch plant table with other tables joined
   async fetchPlantsWithDetails()
   {
      const response = (await this.client({
         url: baseRoute + "alldetails",
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch  a plant with other tables joined
   async fetchOnePlantWithDetails(id: number)
   {
      const response = (await this.client({
         url: baseRoute + id,
         method: 'GET',
      })).data;

      return response;
   }

   //this will save the new or edit of a plant
   async save(dateObtain: string | null, dead: number, deleted: number, highMoisture: number, lowMoisture: number,
      monitor: number, name: string, number: string | null, plantTypeId: number, potSizeId: number, sensorId: number,
      valveId: number | null, username: string, id: string)
   {
      const response = (await this.client({
         url: baseRoute + (id ? id : ''),
         method: 'PUT',
         data: JSON.stringify({
            dateObtain: dateObtain,
            dead: dead,
            delete: deleted,
            high: highMoisture,
            low: lowMoisture,
            monitor: monitor,
            name: name,
            number: number,
            plantTypeId: plantTypeId,
            potSizeId: potSizeId,
            sensorId: sensorId,
            valveId: valveId,
            username: username
         })
      })).data;

      return response;
   }

   //this is to soft delete a plant
   async delete(id: number, username: string)
   {
      const response = (await this.client({
         url: baseRoute + 'delete/' + id,
         method: 'PUT',
         data: JSON.stringify({username})
      })).data;

      return response;
   }

   //this is to activate the monitoring of a plant
   async activate(id: number, username: string)
   {
      const response = (await this.client({
         url: baseRoute + 'activate/' + id,
         method: 'PUT',
         data: JSON.stringify({username})
      })).data;

      return response;
   }

   //this is to deactivate the monitoring of a plant
   async deactivate(id: number, username: string)
   {
      const response = (await this.client({
         url: baseRoute + 'deactivate/' + id,
         method: 'PUT',
         data: JSON.stringify({username})
      })).data;

      return response;
   }
}

export default PlantRoutes;