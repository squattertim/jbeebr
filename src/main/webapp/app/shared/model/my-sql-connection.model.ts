export interface IMySQLConnection {
  id?: string;
  name?: string;
  hostname?: string;
  port?: number;
  dbName?: string;
  username?: string;
  password?: string;
}

export class MySQLConnection implements IMySQLConnection {
  constructor(
    public id?: string,
    public name?: string,
    public hostname?: string,
    public port?: number,
    public dbName?: string,
    public username?: string,
    public password?: string
  ) {}
}
