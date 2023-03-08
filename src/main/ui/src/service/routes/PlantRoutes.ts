const baseRoute = '/plant/';

class PlantRoutes
{
   host: String;
   headers: any;

   constructor(host: String, headers: any)
   {
      this.host = host;
      this.headers = headers;
   }
   
   // this call is to fetch plant table
   async fetchPlants()
   {
      const response = await fetch(this.host + baseRoute, {
         method: 'GET',
         headers: this.headers
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return await data.plants;
   }

   // this call is to fetch plant table with other tables joined
   async fetchPlantsWithDetails()
   {
      const response = await fetch(this.host + baseRoute + "alldetails", {
         method: 'GET',
         headers: this.headers
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return await data.plants;
   }

   // this call is to fetch  a plant with other tables joined
   async fetchOnePlantWithDetails(id: number)
   {
      const response = await fetch(this.host + baseRoute + id, {
         method: 'GET',
         headers: this.headers
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();
      console.log(data);

      return await data;
   }
}

export default PlantRoutes;