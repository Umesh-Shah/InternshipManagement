export interface AlbumData {
    album_id: string;
    album_name: string;
    album_title_img: string | File | null;
}

export interface Album extends Omit<AlbumData, 'album_title_img'> {
    title_image_url: string;
    created_at: string;
    updated_at: string;
}

export interface AddImagesData {
    album_id: string;
    images: File[];
} 