export interface IDashboard {
  id?: string;
  title?: string | null;
  content?: string | null;
  order?: number | null;
}

export class Dashboard implements IDashboard {
  constructor(public id?: string, public title?: string | null, public content?: string | null, public order?: number | null) {}
}
