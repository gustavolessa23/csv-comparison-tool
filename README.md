# CSV Comparison Tool
Command-line application to compare two datasets present in a CSV files, exporting the conflicting fields.


## Parameters
#### Source file (REQUIRED)
```
--src="PATH_TO_CSV_FILE.csv" 
```
#### Destination
If not provided, will point to default Java temp dir.
```
--dest="PATH_TO_DIR" 
```
#### Column containing the dataset identifiers (REQUIRED)
```
--system="Platform" 
```
#### Field options of the above column
Must be 2 parameters, separated by a comma and wrapped in quotes.
```
--options="FC,CV" 
```
#### Column to determine if entries are comparable
```
--same="AccountNumber" 
```
#### Columns to be analysed when comparing entries
One ore more column names, separated by a comma and wrapped in quotes.
```
--compare="Column1,Column4,Column5"
```
#### Output file type
File type with no dash (`--`).
Currently, only `csv` is available.
```
csv
```