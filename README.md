# CSV Comparison Tool
Command-line application to compare two datasets present in a CSV files, exporting the conflicting fields.

## Requirements
- Java 8;
- Maven (tested on version 3.6.3);

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
#### Columns to determine if entries are comparable
One or more parameters that form the composite key.
More than one run is allowed at the same time,
```
--same="AccountNumber,Column1" 
```
#### Columns to be analysed when comparing entries
One or more column names, separated by a comma and wrapped in quotes.
```
--compare="Column2,Column4,Column5"
```
#### Output file type
File type with no dash (`--`).
Currently, only `csv` is available.
```
csv
```