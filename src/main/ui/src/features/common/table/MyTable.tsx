import React, { useState, useMemo } from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';

function MyTable(props: any) {
   const [page, setPage] = useState(0);
   const [rowsPerPage, setRowsPerPage] = useState(10);

   const data: any = useMemo(
      () => {return props.value },
      [props.value]
   );

   const columns: any = useMemo(
      () => props.columns,
      [props.columns]
   );

   const handleChangePage = (event: unknown, newPage: number) => {
      setPage(newPage);
   };

   const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
      setRowsPerPage(+event.target.value);
      setPage(0);
   };

   return (
      <Paper sx={{ width: '100%', overflow: 'hidden' }}>
         <TableContainer sx={{  }}>
         <Table stickyHeader aria-label="sticky table">
            <TableHead>
               <TableRow>
               {columns.map((column: any) => (
                  <TableCell
                     key={column.id}
                     align={column.align}
                     style={{ minWidth: column.minWidth }}
                  >
                     {column.label}
                  </TableCell>
               ))}
               </TableRow>
            </TableHead>
            <TableBody>
               {data
               .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
               .map((row: any) => {
                  return (
                     <TableRow hover role="checkbox" tabIndex={-1} key={row.id}>
                     {columns.map((column: any) => {
                        const value = row[column.id];
                        return (
                           <TableCell key={column.id} align={column.align}>
                           {column.format
                              ? column.format(row, column.id)
                              : value}
                           </TableCell>
                        );
                     })}
                     </TableRow>
                  );
               })}
            </TableBody>
         </Table>
         </TableContainer>
         <TablePagination
            rowsPerPageOptions={[10, 25, 100]}
            component="div"
            count={data.length}
            rowsPerPage={rowsPerPage}
            page={page}
            onPageChange={handleChangePage}
            onRowsPerPageChange={handleChangeRowsPerPage}
         />
      </Paper>
   );
}

export default MyTable;