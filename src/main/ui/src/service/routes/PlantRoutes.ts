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

      return await data;
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

   //this is to soft delete a plant
   async delete(id: number, username: string)
   {
      const response = await fetch(this.host + baseRoute + 'delete/' + id, {
         method: 'PUT',
         headers: this.headers,
         body: JSON.stringify({username})
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return await data;
   }

   //this is to activate the monitoring of a plant
   async activate(id: number, username: string)
   {
      const response = await fetch(this.host + baseRoute + 'activate/' + id, {
         method: 'PUT',
         headers: this.headers,
         body: JSON.stringify({username})
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return await data;
   }

   //this is to deactivate the monitoring of a plant
   async deactivate(id: number, username: string)
   {
      const response = await fetch(this.host + baseRoute + 'deactivate/' + id, {
         method: 'PUT',
         headers: this.headers,
         body: JSON.stringify({username})
      });

      if (!response.ok) throw new Error(`An error has occured: ${response.status}`);
      
      const data: any = await response.json();

      return await data;
   }
}

export default PlantRoutes;