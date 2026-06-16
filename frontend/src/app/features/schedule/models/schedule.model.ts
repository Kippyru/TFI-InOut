export interface ScheduleDto {
    id?: number;
    name: string;
    active?: boolean;
    hourWork: number;
    checkInTolerance: number;
    checkOutTolerance: number;
}

export interface DetailScheduleDto {
    id?: number;
    scheduleId: number;
    day: string;
    checkIn: string; // Using string for LocalTime (e.g., "08:00:00")
    checkOut: string; // Using string for LocalTime
}

export interface ScheduleEmployeeDto {
    id?: number;
    employeeId: number;
    scheduleId: number;
    scheduleName?: string;
    startDate: string; // Using string for LocalDate (e.g., "2023-10-27")
    endDate?: string; // Using string for LocalDate
}
