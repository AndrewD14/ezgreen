const baseRoute = '/sensor/';

class SensorRoutes
{
   client: any;

   constructor(client: any)
   {
      this.client = client;
   }
   
   // this call is to fetch senor configs
   async fetchSensors()
   {
      const response = (await this.client({
         url: baseRoute,
         method: 'GET'
      })).data;

      return response.sensors;
   }

   // this call is to fetch senor configs
   async fetchSensorsWithDetails()
   {
      const response = (await this.client({
         url: baseRoute + 'alldetails',
         method: 'GET',
      })).data;

      return response;
   }

   // this call is to fetch a sensor with all detai infor
   async fetchOneSensorWithDetails(id: number)
   {
      const response = (await this.client({
         url: baseRoute + id,
         method: 'GET',
      })).data;

      return response;
   }

   async fetchSensorCalibration(type: string, board: number, port: number, serialBus: number)
   {
      const response = (await this.client({
         url: baseRoute + "calibration",
         method: 'POST',
         data: JSON.stringify({
            type: type,
            board: board,
            port: port,
            serialBus: serialBus
         })
      })).data;

      return response;
   }

   //this will save the new or edit of a sensor
   async save(type: string, number: number, boardId: number, port: number, lowCalibration: number | null, highCalibration: number | null,
      username: string, id: string)
   {
      const response = (await this.client({
         url: baseRoute + (id ? id : ''),
         method: 'PUT',
         data: JSON.stringify({
            typeId: type,
            number: number,
            boardId: boardId,
            port: port,
            lowCalibration: lowCalibration,
            highCalibration: highCalibration,
            username: username
         })
      })).data;

      return response;
   }
}

export default SensorRoutes;