export function formatAll(data: any)
{
   let relays: any = [];

   relays = data.relays.map((relay: any) => {
      relay.environment = data.environments.filter((environment: any) => (relay.environmentId === environment.id))[0];
      relay.board = data.boards.filter((board: any) => relay.boardId === board.id)[0];
      relay.type = data.relayTypes.filter((relayType: any) => relay.typeId === relayType.id)[0];
      
      return relay;
   });

   return relays;
}

export function formatOption(data: any)
{
   let possibleBoards: any = {};
   let portsUsed: any = {};
   let maxNum: any = {}

   let options: any = {
      boards: data.boards,
      relayTypes: data.relayTypes,
      serialBus: Array.from(new Set(data.boards.map((board: any) => {return board.bus})))
   };

   data.boards.forEach((board: any) => {
      if(possibleBoards[board.bus] === undefined) possibleBoards[board.bus] = [];

      if(possibleBoards[board.bus].indexOf(board.number) < 0) possibleBoards[board.bus].push(board.number);
   });

   data.relays.forEach((relay: any) => {
      let board = {...data.boards.filter((board: any) => board.id === relay.boardId)[0]};

      if(portsUsed[board.bus] === undefined) portsUsed[board.bus] = {};
      if(portsUsed[board.bus][board.number] === undefined) portsUsed[board.bus][board.number] = [];
      
      portsUsed[board.bus][board.number].push(relay.relay);
   });

   data.relayTypes.forEach((relayType: any) => {
      let max = 0;

      data.relays.forEach((relay: any) => {
         if(relay.typeId === relayType.id && relay.number > max) max = relay.number;
      });

      maxNum[relayType.id] = max;
   });

   options['possibleBoards'] = possibleBoards;
   options['portsUsed'] = portsUsed;
   options['maxNum'] = maxNum;

   return options;
}

export function formatOne(data: any)
{
   let relay: any = data.relay;

   relay.environment = data.environment;
   relay.board = data.board;
   relay.type = data.relayType;

   return relay;
}