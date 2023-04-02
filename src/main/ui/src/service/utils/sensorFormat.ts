export function formatAll(data: any)
{
   let sensors: any = [];

   sensors = data.sensors.map((sensor: any) => {
      let environments: any = {};
      let plants: any = {};
      let types: any = {};

      for(let p of data.plants) plants[p.sensorId] = p;
      for(let e of data.environments) environments[e.zoneId+"-"+e.sensorType] = e;
      for(let t of data.sensorTypes) types[t.id] = t;

      for(let s of data.sensors)
      {
         if(plants.hasOwnProperty(s.id)) s.plant = structuredClone(plants[s.id]);
         else if(environments.hasOwnProperty(s.zoneId+"-"+s.typeId)) s.environment = structuredClone(environments[s.zoneId+"-"+s.typeId]);

         s.sensorType = structuredClone(types[s.typeId]);
      }

      return sensor;
   });

   return sensors;
}

export function formatOptions(data: any)
{
   let options: any = {sensorTypes: data.sensorTypes, serialBus: Array.from(new Set(data.boards.map((board: any) => {return board.bus}))), boardInfo: data.boards};
   let possibleBoards: any = {};
   let portsUsed: any = {};

   data.boards.forEach((board: any) => {
      if(possibleBoards[board.bus] === undefined) possibleBoards[board.bus] = [];

      if(possibleBoards[board.bus].indexOf(board.number) < 0) possibleBoards[board.bus].push(board.number);
   });

   options['boards'] = possibleBoards;

   data.sensors.forEach((sensor: any) => {
      let board = {...data.boards.filter((board: any) => board.id === sensor.boardId)[0]};
      let sensorType = {...data.sensorTypes.filter((sensorType: any) => sensorType.id === sensor.typeId)[0]};

      if(portsUsed[board.bus] === undefined) portsUsed[board.bus] = {};
      if(portsUsed[board.bus][board.number] === undefined) portsUsed[board.bus][board.number] = {};
      
      portsUsed[board.bus][board.number][sensor.port] = {
         type: sensorType.id,
         multiple: (sensor.id === 3 ? true : false)
      }
   });

   options['usedPorts'] = portsUsed;

   return options;
}

export function formatOne(data: any)
{
   let sensor: any = {...data.sensor};

   if(data.plant !== undefined) sensor.plant = {...data.plant};
   else if(data.environment !== undefined) sensor.environment = {...data.environment};

   sensor.sensorType = {...data.sensorType};
   sensor.boardInfo = {...data.board};
console.log(sensor)
   return sensor;
}