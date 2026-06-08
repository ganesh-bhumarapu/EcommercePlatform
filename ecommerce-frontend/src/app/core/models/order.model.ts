export interface Order {
  id?: number;
  userId?: number;
  items: OrderItem[];
  totalAmount: number;
  address: string;
  phoneNumber: string;
  orderDate?: Date;
  status?: string;
}

export interface OrderItem {
  productId: number;
  productName: string;
  quantity: number;
  price: number;
}
