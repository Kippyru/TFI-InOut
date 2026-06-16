export interface EventAttendanceDto {
  id?: number;
  employee: number;
  eventType: string;   // 'CHECK_IN' | 'CHECK_OUT'
  hour: string;        // HH:mm:ss from backend
  date: string;        // YYYY-MM-DD
  device: string;
  state: string;
}

export interface AttendanceStatusDto {
  nextEvent: string;               // 'CHECK_IN' | 'CHECK_OUT'
  lastEvent: EventAttendanceDto | null;
  hasSchedule: boolean;
  scheduledCheckIn?: string | null;
  scheduledCheckOut?: string | null;
  checkInTolerance?: number | null;
  checkOutTolerance?: number | null;
}

export interface DailyAttendanceDto {
  employeeId: number;
  numberEmployee: string;
  employeeName: string;
  employeeLastName: string;
  scheduleName: string;
  checkInTime: string | null;
  checkOutTime: string | null;
  checkInEventId: number | null;
  checkOutEventId: number | null;
  reason?: string;
  date?: string;
}
