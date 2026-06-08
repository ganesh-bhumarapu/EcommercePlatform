export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  categoryId: number;
  categoryName?: string;
  quantity: number;
  rating: number;
  inStock: boolean;
}
